package tn.esprit.forum.dao;

import tn.esprit.forum.entity.MostActiveUser;
import tn.esprit.utils.DbConnect;

import java.sql.*;

public class ForumStatsDao {

    private final Connection cnx;

    public ForumStatsDao() throws SQLException {
        cnx = DbConnect.getInstance().getConnection();
    }

    // Active members = distinct users who posted OR commented OR reacted
    public int countActiveMembers() throws SQLException {
        String sql = """
            SELECT COUNT(DISTINCT user_id) AS cnt
            FROM (
                SELECT author_id AS user_id FROM post
                UNION
                SELECT author_id AS user_id FROM comment
                UNION
                SELECT user_id  AS user_id FROM post_reaction
            ) t
            WHERE user_id IS NOT NULL AND user_id <> 0
        """;

        try (PreparedStatement st = cnx.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            rs.next();
            return rs.getInt("cnt");
        }
    }

    // Most active = posts + comments + reactions (highest score)
    public MostActiveUser getMostActiveUser() throws SQLException {
        String sql = """
            SELECT u.id,
                   u.full_name,
                   (COALESCE(p.posts,0) + COALESCE(c.comments,0) + COALESCE(r.reactions,0)) AS score
            FROM user u
            LEFT JOIN (
                SELECT author_id AS uid, COUNT(*) AS posts
                FROM post
                WHERE author_id IS NOT NULL AND author_id <> 0
                GROUP BY author_id
            ) p ON p.uid = u.id
            LEFT JOIN (
                SELECT author_id AS uid, COUNT(*) AS comments
                FROM comment
                WHERE author_id IS NOT NULL AND author_id <> 0
                GROUP BY author_id
            ) c ON c.uid = u.id
            LEFT JOIN (
                SELECT user_id AS uid, COUNT(*) AS reactions
                FROM post_reaction
                GROUP BY user_id
            ) r ON r.uid = u.id
            WHERE u.id IS NOT NULL
            ORDER BY score DESC
            LIMIT 1
        """;

        try (PreparedStatement st = cnx.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
            if (!rs.next()) return null;

            return new MostActiveUser(
                    rs.getInt("id"),
                    rs.getString("full_name"),
                    rs.getInt("score")
            );
        }
    }
}
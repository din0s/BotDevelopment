package org.acm.auth.managers;

import io.github.cdimascio.dotenv.Dotenv;
import org.acm.auth.db.Tables;
import org.acm.auth.db.tables.records.MessagesRecord;
import org.jooq.*;
import org.jooq.impl.DSL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MessageManager {
    private static Connection conn;
    private static DSLContext create;

    public static void init() throws SQLException {
        Dotenv env = Dotenv.load();
        String url = "jdbc:mariadb://localhost:3306";
        String username = env.get("MYSQL_USER");
        String password = env.get("MYSQL_PASSWORD");

        conn = DriverManager.getConnection(url, username, password);
        create = DSL.using(conn, SQLDialect.MARIADB);
    }

    public static void close() throws SQLException {
        conn.close();
    }

    public static boolean saveMessage(String msgId, String content, String userId, String userTag) {
        MessagesRecord msg = create.newRecord(Tables.MESSAGES);
        msg.setId(msgId);
        msg.setContent(content);
        msg.setUserid(userId);
        msg.setUsertag(userTag);
        return msg.store() == 1;
    }

    public static Result<Record1<String>> retrieveUserTag(String content) {
        return create
                .select(Tables.MESSAGES.USERTAG)
                .from(Tables.MESSAGES)
                .where(Tables.MESSAGES.CONTENT.eq(content))
                .fetch();
    }
}

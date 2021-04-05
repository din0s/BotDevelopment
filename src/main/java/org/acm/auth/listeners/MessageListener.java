package org.acm.auth.listeners;

import net.dv8tion.jda.api.events.ShutdownEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.acm.auth.managers.MessageManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class MessageListener extends ListenerAdapter {
    private static final Logger LOGGER = LogManager.getLogger();

    public MessageListener() {
        try {
            MessageManager.init();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        String msgId = event.getMessageId();
        String content = event.getMessage().getContentDisplay();
        String userId = event.getAuthor().getId();
        String userTag = event.getAuthor().getAsTag();
        LOGGER.info("{} {} {} {}", msgId, content, userId, userTag);
        MessageManager.saveMessage(msgId, content, userId, userTag);
    }

    @Override
    public void onShutdown(ShutdownEvent event) {
        try {
            MessageManager.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

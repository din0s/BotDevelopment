package org.acm.auth.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.acm.auth.db.Tables;
import org.acm.auth.managers.MessageManager;
import org.jooq.Record1;
import org.jooq.Result;

public class LookupCommand extends Command {
    public LookupCommand() {
        super(
                "lookup",
                "Looks up a message",
                false, // guildOnly
                false, // devOnly
                new String[]{"search"}, // alias
                1, // minArgs
                Integer.MAX_VALUE, // maxArgs
                "msg query", // usage
                Permission.EMPTY_PERMISSIONS, // bot perms
                Permission.EMPTY_PERMISSIONS // user perms
        );
    }

    @Override
    public void invoke(MessageReceivedEvent event, String[] args) {
        String messageContent = event.getMessage().getContentDisplay();
        int spaceIndex = messageContent.indexOf(' ');
        String lookup = messageContent.substring(spaceIndex + 1).trim();

        Result<Record1<String>> results = MessageManager.retrieveUserTag(lookup);
        int resultsCount = results.size();
        if (resultsCount == 0) {
            event.getChannel().sendMessage("No messages were found!").queue();
        } else if (resultsCount > 1) {
            event.getChannel().sendMessage("Too many messages were found!").queue();
        } else {
            String userTag = results.get(0).get(Tables.MESSAGES.USERTAG);
            event.getChannel().sendMessage(String.format("%s said that!", userTag)).queue();
        }
    }
}

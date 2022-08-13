package uz.maniac4j.forwarder.bot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Component
@RequiredArgsConstructor
public class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    @Getter
    private String botUsername;

    @Value("${bot.token}")
    @Getter
    private String botToken;

    @Value("${bot.admin.id}")
    @Getter
    private String botAdminId;

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update);


        if (update.hasMessage()){
            String message=update.getMessage().getText();
            if (message!=null&&(message.equalsIgnoreCase("/start")&& !update.getMessage().getChatId().toString().equals(botAdminId))){
                SendMessage response=new SendMessage();
                response.setChatId(update.getMessage().getChatId());
                response.setText("Men bilan gaplashing");
                try {
                    execute(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {

                ForwardMessage forwardMessage = new ForwardMessage();
                forwardMessage.setChatId(botAdminId);
                forwardMessage.setFromChatId(update.getMessage().getChatId());
                forwardMessage.setMessageId(update.getMessage().getMessageId());
                try {
                    execute(forwardMessage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }
}


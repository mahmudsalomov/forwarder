package uz.maniac4j.forwarder.bot;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.groupadministration.BanChatMember;
import org.telegram.telegrambots.meta.api.methods.groupadministration.UnbanChatMember;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.maniac4j.forwarder.model.Sender;
import uz.maniac4j.forwarder.service.SenderService;

import java.util.Objects;

@Service
@Slf4j
@Component
public class Bot extends TelegramLongPollingBot {


    private final SenderService senderService;


    @Value("${bot.name}")
    @Getter
    private String botUsername;

    @Value("${bot.token}")
    @Getter
    private String botToken;

    @Value("${bot.admin.id}")
    @Getter
    private String botAdminId;

    public Bot(SenderService senderService) {
        this.senderService = senderService;
    }

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

        BanChatMember banChatMember=new BanChatMember();



        if (update.hasMessage()){
            if (!senderService.isBan(update.getMessage().getChatId())){
                Sender sender = senderService.save(update.getMessage().getFrom());
                System.out.println(sender);


                Message replyToMessage = update.getMessage().getReplyToMessage();
                String message=update.getMessage().getText();



                //If message reply

                if (replyToMessage!=null&& !Objects.equals(replyToMessage.getChatId().toString(), replyToMessage.getForwardFrom().getId().toString()) && update.getMessage().getChatId().toString().equals(botAdminId)){

                    System.out.println("AAAAAA = "+replyToMessage.getChatId());
                    System.out.println("AAAAAA = "+replyToMessage.getForwardFrom().getId());
                    if (message.equals("/ban")||message.equals("/unban")){
                        switch (message){
                            case "/ban"->senderService.ban(replyToMessage.getForwardFrom().getId());
                            case "/unban"->senderService.unBan(replyToMessage.getForwardFrom().getId());
                        }
                    }
                    else {
                        send(replyToMessage.getForwardFrom().getId().toString(),update.getMessage().getText());
                    }

                }
                else {





                    if (message!=null&&(message.equalsIgnoreCase("/start")&& !update.getMessage().getChatId().toString().equals(botAdminId))){
                        send(update.getMessage().getChatId().toString(),"Men bilan gaplashing");
                    } else {

                        System.out.println("FORWARD");
                        System.out.println(update.getMessage());
                        System.out.println(update.getMessage().getForwardFrom());
                        System.out.println(update.getMessage().getForwardSenderName());
                        System.out.println(update.getMessage().getForwardFromChat());

                        if (
                                update.getMessage().getForwardFrom()!=null
                                || update.getMessage().getForwardSenderName()!=null
                                || update.getMessage().getForwardFromChat()!=null
                        ){
                            send(update.getMessage().getChatId().toString(),"Forward qabul qilinmaydi!");
                        }

                        else {
                            ForwardMessage forwardMessage = new ForwardMessage();
                            forwardMessage.setChatId(botAdminId);
//                        update.getMessage().getForwardFrom().getId()
                            forwardMessage.setFromChatId(update.getMessage().getFrom().getId());
//                        forwardMessage.setFromChatId(update.getMessage().getForwardFrom().getId());
                            forwardMessage.setMessageId(update.getMessage().getMessageId());

                            send(forwardMessage);
                        }

                    }
                }
            }






        }

    }


    public void send(String chatId,String text){
        SendMessage response=new SendMessage();
        response.setChatId(chatId);
        response.setText(text);
        send(response);
    }


    public void send(BotApiMethodMessage message){
        try {
            execute(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}


package uz.maniac4j.forwarder.service;

import lombok.Getter;
import org.jvnet.hk2.annotations.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.User;
import uz.maniac4j.forwarder.model.Sender;
import uz.maniac4j.forwarder.repository.SenderRepository;

@Service
@Component
public class SenderService {

    private final SenderRepository senderRepository;

    @Value("${bot.admin.id}")
    @Getter
    private String botAdminId;
    public SenderService(SenderRepository senderRepository) {
        this.senderRepository = senderRepository;
    }


    public Sender save(User user){
        try {
            if (!senderRepository.existsById(user.getId()) && !user.getId().toString().equals(botAdminId))
            return senderRepository.save(new Sender(user));
            else {
                if (user.getId().toString().equals(botAdminId)) return null;
                return senderRepository.findById(user.getId()).get();
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }



    /**BAN & UNBAN**/

    public void ban(Sender sender){
        try {
            senderRepository.save(sender.ban());
            System.out.println("User : "+sender.getId()+" is banned");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void ban(Long id){
        try {
            senderRepository.save(senderRepository.findById(id).get().ban());
            System.out.println("User : "+id+" is banned");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void unBan(Sender sender){
        try {
            senderRepository.save(sender.unBan());
            System.out.println("User : "+sender.getId()+" is unbanned");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void unBan(Long id){
        try {
            senderRepository.save(senderRepository.findById(id).get().unBan());
            System.out.println("User : "+id+" is unbanned");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isBan(Long id){
        if (id.toString().equals(botAdminId)) return false;
        return senderRepository.existsSenderByIdAndBanTrue(id);
    }
}

package com.jpmc.midascore.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.UserRepository;
import java.util.Optional;

@Component
@Transactional
public class DatabaseConduit {

    @Autowired
    private UserRepository userRepository;

    public void save(UserRecord user) {
        userRepository.save(user);
    }

    public void process(Transaction transaction) {

        Optional<UserRecord> senderOpt = userRepository.findById(transaction.getSenderId());
        Optional<UserRecord> recipientOpt = userRepository.findById(transaction.getRecipientId());

        if (senderOpt.isEmpty() || recipientOpt.isEmpty()) return;

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        if (sender.getBalance() < transaction.getAmount()) return;

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());

        userRepository.save(sender);
        userRepository.save(recipient);

        Optional<UserRecord> waldorfOpt = userRepository.findByName("waldorf");
        waldorfOpt.ifPresent(w ->
            System.out.println("WALDORF FINAL BALANCE = " + w.getBalance())
        );
    }
}
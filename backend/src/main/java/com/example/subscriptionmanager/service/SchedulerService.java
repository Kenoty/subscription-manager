package com.example.subscriptionmanager.service;

import com.example.subscriptionmanager.entity.Subscription;
import com.example.subscriptionmanager.entity.SubscriptionEvent;
import com.example.subscriptionmanager.repository.SubscriptionEventRepository;
import com.example.subscriptionmanager.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionEventRepository subscriptionEventRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 9 * * *")
    public void checkExpiringSubscriptions() {
        List<Subscription> subscriptions = subscriptionRepository.findAll();

        for (Subscription subscription : subscriptions) {
            List<SubscriptionEvent> events = subscriptionEventRepository
                    .findBySubscriptionId(subscription.getId());

            events.stream()
                    .filter(e -> e.getDays() != null)
                    .reduce((first, second) -> second)
                    .ifPresent(lastEvent -> {
                        LocalDate expiryDate = lastEvent.getEventDate().plusDays(lastEvent.getDays());
                        long daysLeft = java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);

                        if (daysLeft == 3) {
                            notificationService.createForUser(subscription.getUser(),
                                    "Подписка на " + subscription.getPlan().getService().getName()
                                    + " истекает через 3 дня");
                        } else if (daysLeft == 0) {
                            notificationService.createForUser(subscription.getUser(),
                                    "Подписка на " + subscription.getPlan().getService().getName()
                                    + " истекла сегодня");
                        }
                    });
        }
    }
}

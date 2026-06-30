package com.example.subscriptionmanager.service;

import com.example.subscriptionmanager.dto.request.CreateSubscriptionRequest;
import com.example.subscriptionmanager.dto.response.SubscriptionEventResponse;
import com.example.subscriptionmanager.dto.response.SubscriptionResponse;
import com.example.subscriptionmanager.entity.*;
import com.example.subscriptionmanager.exception.ResourceNotFoundException;
import com.example.subscriptionmanager.mapper.SubscriptionEventMapper;
import com.example.subscriptionmanager.mapper.SubscriptionMapper;
import com.example.subscriptionmanager.repository.*;
import com.example.subscriptionmanager.security.CurrentUserProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;
    private final SubscriptionEventRepository subscriptionEventRepository;
    private final PaymentRepository paymentRepository;
    private final EventTypeRepository eventTypeRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionEventMapper subscriptionEventMapper;
    private final CurrentUserProvider currentUserProvider;
    private final NotificationService notificationService;

    public List<SubscriptionResponse> getMySubscriptions() {
        return subscriptionRepository.findByUserIdAndCancelledAtIsNull(currentUserProvider.getCurrentUserId())
                .stream().map(subscriptionMapper::toResponse).toList();
    }

    public SubscriptionResponse getById(Integer id) {
        return subscriptionMapper.toResponse(findSubscriptionOwnedByCurrentUser(id));
    }

    @Transactional
    public SubscriptionResponse create(CreateSubscriptionRequest request) {
        Plan plan = planRepository.findById(request.getPlanId())
                .orElseThrow(() -> new ResourceNotFoundException("Plan not found"));

        User user = currentUserProvider.getCurrentUser();

        Subscription subscription = new Subscription();
        subscription.setUser(user);
        subscription.setPlan(plan);
        subscription.setAutoRenew(request.getAutoRenew());
        Subscription saved = subscriptionRepository.save(subscription);

        SubscriptionEvent event = createEvent(saved, "activated", 30);
        createPayment(event, plan.getPrice(), plan.getCurrency());
        notificationService.createForUser(user,
                "Подписка на " + plan.getService().getName() + " — " + plan.getName() + " активирована");

        return subscriptionMapper.toResponse(saved);
    }

    public SubscriptionResponse toggleAutoRenew(Integer id) {
        Subscription subscription = findSubscriptionOwnedByCurrentUser(id);
        subscription.setAutoRenew(!subscription.getAutoRenew());
        return subscriptionMapper.toResponse(subscriptionRepository.save(subscription));
    }

    @Transactional
    public void cancel(Integer id) {
        Subscription subscription = findSubscriptionOwnedByCurrentUser(id);
        subscription.setCancelledAt(OffsetDateTime.now());
        subscriptionRepository.save(subscription);
        createEvent(subscription, "cancelled", null);
        notificationService.createForUser(subscription.getUser(),
                "Подписка на " + subscription.getPlan().getService().getName() + " отменена");
    }

    public List<SubscriptionEventResponse> getEvents(Integer id) {
        findSubscriptionOwnedByCurrentUser(id);
        return subscriptionEventRepository.findBySubscriptionId(id)
                .stream().map(subscriptionEventMapper::toResponse).toList();
    }

    public List<SubscriptionResponse> getAllSubscriptions() {
        return subscriptionRepository.findAll()
                .stream().map(subscriptionMapper::toResponse).toList();
    }

    private SubscriptionEvent createEvent(Subscription subscription, String eventTypeName, Integer days) {
        EventType eventType = eventTypeRepository.findByName(eventTypeName)
                .orElseThrow(() -> new ResourceNotFoundException("Event type not found: " + eventTypeName));
        SubscriptionEvent event = new SubscriptionEvent();
        event.setSubscription(subscription);
        event.setEventType(eventType);
        event.setEventDate(LocalDate.now());
        event.setDays(days);
        return subscriptionEventRepository.save(event);
    }

    private void createPayment(SubscriptionEvent event, BigDecimal amount, String currency) {
        Payment payment = new Payment();
        payment.setSubscriptionEvent(event);
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setPaidAt(OffsetDateTime.now());
        paymentRepository.save(payment);
    }

    private Subscription findSubscriptionOwnedByCurrentUser(Integer id) {
        return subscriptionRepository.findByIdAndUserIdAndCancelledAtIsNull(id, currentUserProvider.getCurrentUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Subscription not found"));
    }
}

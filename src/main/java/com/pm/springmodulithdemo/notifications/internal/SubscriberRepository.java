package com.pm.springmodulithdemo.notifications.internal;

import org.springframework.data.repository.ListCrudRepository;

public interface SubscriberRepository extends ListCrudRepository<Subscriber, Long> {
}

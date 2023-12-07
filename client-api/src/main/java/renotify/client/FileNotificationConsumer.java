package renotify.client;

import renotify.common.Notification;

public interface FileNotificationConsumer {

    void consume(Notification notification);
}

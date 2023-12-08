package imnasser.projects.rfwatcher.client;

import imnasser.projects.rfwatcher.common.Notification;

public interface FileNotificationConsumer {

    void consume(Notification notification);
}

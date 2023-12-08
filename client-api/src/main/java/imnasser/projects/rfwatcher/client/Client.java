package imnasser.projects.rfwatcher.client;

import imnasser.projects.rfwatcher.common.FileSubscription;


public interface Client {

    void register(FileSubscription subscription, FileNotificationConsumer consumer);

    void streamNotifications(FileNotificationConsumer consumer);

    /*
     * TODO - unregister file
     *
     * TODO - Add close method
     */
}

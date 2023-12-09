package imnasser.projects.rws.server;

import imnasser.projects.rws.Notification;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchKey;

public interface NotificationService {

    public WatchKey add(Path file, int events) throws IOException;

    public void remove(Path file);

    public Notification read() throws InterruptedException;

    public void close() throws IOException;

}

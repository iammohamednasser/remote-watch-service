package imnasser.projects.rfwatcher.grpc.server;

import io.grpc.stub.StreamObserver;

import imnasser.projects.rws.grpc.Empty;
import imnasser.projects.rws.Notification;
import imnasser.projects.rws.grpc.GrpcFileSubscription;
import imnasser.projects.rws.grpc.GrpcNotification;
import imnasser.projects.rws.grpc.RemoteWatchServiceGrpc;
import imnasser.projects.rws.server.NotificationServiceDefaultImpl;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RenotifyService extends RemoteWatchServiceGrpc.RemoteWatchServiceImplBase {

    NotificationServiceDefaultImpl notificationServiceDefaultImpl = new NotificationServiceDefaultImpl();

    public RenotifyService() throws IOException {
    }


    @Override
    public void register(GrpcFileSubscription subscription, StreamObserver<GrpcNotification> responseObserver) {
        Path file = Paths.get(subscription.getFilePath());
        int events = subscription.getEvents();
        try {
            this.notificationServiceDefaultImpl.add(file, events);
            GrpcNotification added = GrpcNotification
                    .newBuilder()
                    .setPendingEvents(8)
                    .setFile(file.toString())
                    .build();
            responseObserver.onNext(added); // TODO
        } catch (IOException ignored) {
        }
    }

    @Override
    public void streamNotifications(Empty request, StreamObserver<GrpcNotification> responseObserver) {
        while (true) {
            try {
                Notification notification = this.notificationServiceDefaultImpl.get();

                responseObserver.onNext(grpcNotification(notification));
            } catch (InterruptedException e) {
                responseObserver.onError(e);
            }
        }
    }

    private static GrpcNotification grpcNotification(Notification notification) {
        return GrpcNotification.newBuilder()
                .setFile(notification.path().toString())
                .setContext(notification.context().toString())
                .setPendingEvents(notification.events())
                .build();
    }
}

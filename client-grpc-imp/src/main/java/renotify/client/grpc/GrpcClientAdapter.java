package renotify.client.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import renotify.client.Client;
import renotify.client.FileNotificationConsumer;
import renotify.grpc.Empty;
import renotify.grpc.GrpcFileSubscription;
import renotify.common.FileSubscription;
import renotify.common.Notification;
import renotify.grpc.GrpcNotification;
import renotify.grpc.RenotifyGrpc;

import java.net.InetSocketAddress;
import java.nio.file.Paths;

public class GrpcClientAdapter implements Client {

    // private final ManagedChannel channel;

    private final RenotifyGrpc.RenotifyStub stub;

    private static final Empty EMPTY = Empty.newBuilder().build();

    private GrpcClientAdapter(ManagedChannel channel) {
        this.stub = RenotifyGrpc.newStub(channel);
    }

    public static GrpcClientAdapter createClient(InetSocketAddress serverAddress) {
        String host = serverAddress.getHostName();
        int port = serverAddress.getPort();
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress(host, port)
                .usePlaintext()
                .build();
        return new GrpcClientAdapter(channel);
    }

    @Override
    public void register(FileSubscription subscription, FileNotificationConsumer consumer) {
        this.stub.register(
                grpcSubscription(subscription),
                streamObserver(consumer)
        );
    }

    @Override
    public void streamNotifications(FileNotificationConsumer consumer) {
        this.stub.streamNotifications(EMPTY, streamObserver(consumer));
    }

    private static StreamObserver<GrpcNotification> streamObserver(FileNotificationConsumer consumer) {

        return new StreamObserver<GrpcNotification>() {
            @Override
            public void onNext(GrpcNotification value) {
                consumer.consume(notification(value));
            }

            @Override
            public void onError(Throwable t) {
                // TODO - Do something and unregister file and consumer
            }

            @Override
            public void onCompleted() {
                // TODO - unregister file and consumer
            }
        };
    }

    private static GrpcFileSubscription grpcSubscription(FileSubscription subscription) {
        return GrpcFileSubscription.newBuilder()
                .setFilePath(subscription.path().toString())
                .setEvents(subscription.events())
                .build();
    }

    private static Notification notification(GrpcNotification notification) {
        return new Notification(
                Paths.get(notification.getFile()),
                Paths.get(notification.getContext()),
                notification.getPendingEvents()
        );
    }
}

package imnasser.projects.rfwatcher.common;

import java.nio.file.Path;

public record FileSubscription(Path path, int events) {
}

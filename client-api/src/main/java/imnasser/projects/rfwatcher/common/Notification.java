package imnasser.projects.rfwatcher.common;

import java.nio.file.Path;

public record Notification(Path path, Path context, int events) {
}

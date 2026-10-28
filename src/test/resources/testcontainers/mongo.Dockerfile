# Image used by TestcontainersConfiguration; kept here so Dependabot can update it.
# MongoDB 8.0 refuses to start on Linux kernels 6.19-7.0.13 (SERVER-121912), so stay on 9.0+.
FROM mongo:9.0.4

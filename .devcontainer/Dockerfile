# Development container for Quarkus Lambda with GraalVM native build support
# Based on Ubuntu for compatibility with devcontainer features (docker-in-docker)

FROM ubuntu:22.04

# Avoid prompts during package installation
ENV DEBIAN_FRONTEND=noninteractive

# Install development tools and dependencies
RUN apt-get update && apt-get install -y \
    git \
    curl \
    unzip \
    tar \
    gzip \
    findutils \
    procps \
    sudo \
    build-essential \
    zlib1g-dev \
    libfreetype6-dev \
    ca-certificates \
    zsh \
    && rm -rf /var/lib/apt/lists/*

# Install GraalVM Community Edition with native-image (supports both amd64 and arm64)
ARG GRAALVM_VERSION=17.0.9
ARG TARGETARCH
RUN ARCH=$([ "$TARGETARCH" = "arm64" ] && echo "aarch64" || echo "x64") && \
    GRAALVM_PKG="graalvm-community-jdk-${GRAALVM_VERSION}_linux-${ARCH}_bin.tar.gz" && \
    curl -fsSL "https://github.com/graalvm/graalvm-ce-builds/releases/download/jdk-${GRAALVM_VERSION}/${GRAALVM_PKG}" \
    | tar -xzf - -C /opt && \
    mv /opt/graalvm-community-openjdk-${GRAALVM_VERSION}* /opt/graalvm

# Set GraalVM environment variables
ENV JAVA_HOME=/opt/graalvm
ENV GRAALVM_HOME=/opt/graalvm
ENV PATH="${GRAALVM_HOME}/bin:${PATH}"

# Install Maven 3.9.x
ARG MAVEN_VERSION=3.9.9
RUN curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    | tar -xzf - -C /opt \
    && ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven \
    && ln -s /opt/maven/bin/mvn /usr/local/bin/mvn

# Install AWS SAM CLI (pinned version for reproducible builds, supports both amd64 and arm64)
ARG SAM_CLI_VERSION=1.133.0
RUN ARCH=$(dpkg --print-architecture) && \
    if [ "$ARCH" = "arm64" ]; then SAM_ARCH="arm64"; else SAM_ARCH="x86_64"; fi && \
    curl -fsSL "https://github.com/aws/aws-sam-cli/releases/download/v${SAM_CLI_VERSION}/aws-sam-cli-linux-${SAM_ARCH}.zip" -o /tmp/sam.zip \
    && unzip /tmp/sam.zip -d /tmp/sam \
    && /tmp/sam/install \
    && rm -rf /tmp/sam /tmp/sam.zip

# Install AWS CLI v2 (supports both amd64 and arm64)
RUN ARCH=$(dpkg --print-architecture) && \
    if [ "$ARCH" = "arm64" ]; then AWS_ARCH="aarch64"; else AWS_ARCH="x86_64"; fi && \
    curl -fsSL "https://awscli.amazonaws.com/awscli-exe-linux-${AWS_ARCH}.zip" -o /tmp/awscli.zip \
    && unzip /tmp/awscli.zip -d /tmp \
    && /tmp/aws/install \
    && rm -rf /tmp/aws /tmp/awscli.zip

# Set Maven environment
ENV MAVEN_HOME=/opt/maven

# Create non-root user for development
ARG USERNAME=vscode
ARG USER_UID=1000
ARG USER_GID=$USER_UID

RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m -s /bin/zsh $USERNAME \
    && echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME

# Set working directory
WORKDIR /workspace

# Switch to non-root user
USER $USERNAME

# Default command
CMD ["zsh"]

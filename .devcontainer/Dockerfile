# Development container for Quarkus Lambda with GraalVM native build support
# Based on GraalVM Community Edition with Java 17

FROM ghcr.io/graalvm/graalvm-community:17

# Install native-image component for GraalVM native builds
RUN gu install native-image

# Install development tools and dependencies
RUN microdnf install -y \
    git \
    curl \
    unzip \
    tar \
    gzip \
    findutils \
    which \
    procps-ng \
    && microdnf clean all

# Install Maven 3.9.x
ARG MAVEN_VERSION=3.9.9
RUN curl -fsSL https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz \
    | tar -xzf - -C /opt \
    && ln -s /opt/apache-maven-${MAVEN_VERSION} /opt/maven \
    && ln -s /opt/maven/bin/mvn /usr/local/bin/mvn

# Install AWS SAM CLI (pinned version for reproducible builds)
ARG SAM_CLI_VERSION=1.133.0
RUN curl -fsSL https://github.com/aws/aws-sam-cli/releases/download/v${SAM_CLI_VERSION}/aws-sam-cli-linux-x86_64.zip -o /tmp/sam.zip \
    && unzip /tmp/sam.zip -d /tmp/sam \
    && /tmp/sam/install \
    && rm -rf /tmp/sam /tmp/sam.zip

# Install AWS CLI v2
RUN curl -fsSL https://awscli.amazonaws.com/awscli-exe-linux-x86_64.zip -o /tmp/awscli.zip \
    && unzip /tmp/awscli.zip -d /tmp \
    && /tmp/aws/install \
    && rm -rf /tmp/aws /tmp/awscli.zip

# Set environment variables
ENV JAVA_HOME=/usr/lib64/graalvm/graalvm-community-java17
ENV GRAALVM_HOME=/usr/lib64/graalvm/graalvm-community-java17
ENV MAVEN_HOME=/opt/maven
ENV PATH="${JAVA_HOME}/bin:${MAVEN_HOME}/bin:${PATH}"

# Create non-root user for development
ARG USERNAME=vscode
ARG USER_UID=1000
ARG USER_GID=$USER_UID

RUN groupadd --gid $USER_GID $USERNAME \
    && useradd --uid $USER_UID --gid $USER_GID -m $USERNAME \
    && microdnf install -y sudo \
    && echo $USERNAME ALL=\(root\) NOPASSWD:ALL > /etc/sudoers.d/$USERNAME \
    && chmod 0440 /etc/sudoers.d/$USERNAME \
    && microdnf clean all

# Set working directory
WORKDIR /workspace

# Switch to non-root user
USER $USERNAME

# Default command
CMD ["bash"]

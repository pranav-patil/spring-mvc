package com.library.spring.web.model;

public class VersionBean {

    private String artifactId;
    private String version;
    private String buildTimestamp;
    private String environment;
    private String manifestVersion;
    private String scmVersion;

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getBuildTimestamp() {
        return buildTimestamp;
    }

    public void setBuildTimestamp(String buildTimestamp) {
        this.buildTimestamp = buildTimestamp;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String getManifestVersion() {
        return manifestVersion;
    }

    public void setManifestVersion(String manifestVersion) {
        this.manifestVersion = manifestVersion;
    }

    public String getScmVersion() {
        return scmVersion;
    }

    public void setScmVersion(String scmVersion) {
        this.scmVersion = scmVersion;
    }
}

package ca.corbett.tasktracker.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a Project, which has zero or more ProjectVersions that link to it, as well
 * as zero or more Tickets that link to it. Internally, we use the internalId field to
 * reference other model objects. The internalId field is never surfaced in the UI.
 */
public class Project {
    protected UUID internalId;
    protected String prefix;
    protected String name;
    protected String description;
    protected Date startDate;
    protected Integer bgColor;
    protected Integer fgColor;

    @JsonIgnore
    protected File sourceFile;

    @JsonIgnore
    protected final static ObjectMapper objectMapper;

    protected Project() { }

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static Project load(File srcFile) throws IOException {
        Project project = objectMapper.readValue(srcFile, Project.class);
        project.sourceFile = srcFile;
        return project;
    }

    public static Project create(String prefix, String name) {
        Project project = new Project();
        project.prefix = prefix;
        project.name = name;
        return project;
    }

    public Project setDescription(String desc) {
        description = desc;
        return this;
    }

    public Project setStartDate(Date date) {
        startDate = date;
        return this;
    }

    public Project setColors(Color bg, Color fg) {
        bgColor = bg.getRGB();
        fgColor = fg.getRGB();
        return this;
    }

    public UUID getInternalId() {
        return internalId;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Color getBgColor() {
        return new Color(bgColor);
    }

    public Color getFgColor() {
        return new Color(fgColor);
    }

    public File getSourceFile() {
        return sourceFile;
    }
}

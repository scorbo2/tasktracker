package ca.corbett.tasktracker.model;

import ca.corbett.tasktracker.date.YMDDate;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
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
    protected YMDDate startDate;
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

    /**
     * Instantiates a Project from a json file containing all metadata.
     *
     * @param srcFile A Project file in json format.
     * @return A populated Project instance.
     * @throws IOException If the file cannot be loaded.
     */
    public static Project load(File srcFile) throws IOException {
        Project project = objectMapper.readValue(srcFile, Project.class);
        project.sourceFile = srcFile;
        return project;
    }

    /**
     * Creates a new Project instance with the given name and prefix. An internalId
     * will be assigned automatically, but the Project will not exist on disk
     * until save() is invoked.
     *
     * @param prefix The non-empty Project prefix.
     * @param name   The non-empty Project name.
     * @return A Project instance with an auto-assigned internalId.
     */
    public static Project create(String prefix, String name) {
        Project project = new Project();
        project.internalId = UUID.randomUUID();
        project.prefix = prefix;
        project.name = name;
        return project;
    }

    public Project setDescription(String desc) {
        description = desc;
        return this;
    }

    public Project setStartDate(YMDDate date) {
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

    public YMDDate getStartDate() {
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

    /**
     * Checks the original file on disk (if one has been created for this Project)
     * and reports whether any differences exist between this instance in memory
     * and that version on disk. There are two possible scenarios here:
     * <ol>
     *     <li>This instance was loaded from disk and has been changed here in memory.
     *     <li>This instance was loaded from disk and some external process then edited the file.
     * </ol>
     * If any differences exist, we return true. If this Project has not yet been saved to disk,
     * we also return true.
     *
     * @return true if any differences exist between this instance and the disk version.
     * @throws IOException If the source file can no longer be loaded.
     */
    public boolean isDirty() throws IOException {
        // If we have not yet been saved to disk, then we're dirty:
        if (sourceFile == null) {
            return true;
        }

        // If the source file has since been deleted, we are dirty:
        if (sourceFile.exists()) {
            return true;
        }

        String currentFileContent = Files.readString(sourceFile.toPath());
        String currentObjectJson = objectMapper.writeValueAsString(this);

        // Using readTree should ignore any formatting differences (properties in the
        // wrong order, whitespace in the json, etc).
        try {
            JsonNode fileNode = objectMapper.readTree(currentFileContent);
            JsonNode objectNode = objectMapper.readTree(currentObjectJson);
            return !fileNode.equals(objectNode);
        }
        catch (Exception e) {
            return true; // If we can't parse, assume dirty
        }
    }

    /**
     * Saves this Project instance to disk. For first-time saves, this will create a new Project file
     * on disk. For subsequent saves, the Project file is overwritten with the new values.
     *
     * @throws IOException If something goes wrong.
     */
    public void save(File f) throws IOException {
//        if (sourceFile == null) {
//            // TODO first-time save... generate new file... we need a "project dir"
//            throw new IOException("Cannot save: no source path set");
//        }
        this.sourceFile = f; // TEMP TEMP TODO REMOVE ME

        // Save project.json
        objectMapper.writeValue(sourceFile, this);

        // TODO also save all tickets
        //File ticketsDir = new File(sourceFile.getParent(), "tickets");
        //ticketsDir.mkdirs();
        //for (Ticket ticket : tickets) {
        //    if (ticket.isDirty()) {
        //        ticket.save();
        //    }
        //}
    }
}

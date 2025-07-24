package ca.corbett.tasktracker.model;

import java.util.Date;
import java.util.UUID;

public class ProjectVersion {
    protected UUID internalId;
    protected Date startDate;
    protected Date releaseDate;
    protected String label;
    protected String description;
}

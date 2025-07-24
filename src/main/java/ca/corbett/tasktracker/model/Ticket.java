package ca.corbett.tasktracker.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Ticket {
    protected UUID internalId;
    protected UUID projectId;
    protected UUID targetVersionId;
    protected int displayId;
    protected Date createDate;
    protected Date startDate;
    protected Date closeDate;
    protected String shortDescription;
    protected String longDescription;
    protected Double hoursWorked;
    protected String state;
    protected String resolution;
    protected final List<TicketComment> comments = new ArrayList<>();
}

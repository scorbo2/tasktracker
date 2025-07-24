package ca.corbett.tasktracker.model;

import java.util.Date;
import java.util.UUID;

public class TicketComment {
    protected UUID internalId;
    protected UUID ticketId;
    protected Date createDate;
    protected Date lastEditDate;
    protected String commentText;
}

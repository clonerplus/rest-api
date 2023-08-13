package ir.sobhan.restapi.model.individual;

import java.util.Date;

public class Student {
    private enum Degree{BS, MS, PHD}
    private String studentId;
    private Date startDate;
    private Degree degree;

}

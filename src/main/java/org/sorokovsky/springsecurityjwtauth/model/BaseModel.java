package org.sorokovsky.springsecurityjwtauth.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BaseModel {
    private Long id;
    private Date createdAt;
    private Date updatedAt;
}

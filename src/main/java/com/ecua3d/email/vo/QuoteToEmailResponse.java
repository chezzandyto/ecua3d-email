package com.ecua3d.email.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuoteToEmailResponse {
    private String name;
    private String email;
    private String phone;
    private Integer filamentId;
    private Integer qualityId;
    private List<FileResponse> files;
    private String comment;
}

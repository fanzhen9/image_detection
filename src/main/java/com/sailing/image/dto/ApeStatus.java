package com.sailing.image.dto;

import lombok.Data;

@Data
public class ApeStatus {

    private String apeId;

    private String status;

    public ApeStatus() {
    }

    public ApeStatus(String apeId) {
        this.apeId = apeId;
    }

    public ApeStatus(String apeId, String status) {
        this.apeId = apeId;
        this.status = status;
    }
}

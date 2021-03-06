package io.softserve.goadventures.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UploadFileResponse {   //Response info about file to client
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private long size;

}

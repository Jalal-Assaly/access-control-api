package org.pacs.accesscontrolapi.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@Document(collection = "nonceTrackers")
public class NonceTracker {
    @Id
    String userID;
    List<String> nonceSequence;
    Integer index;
}

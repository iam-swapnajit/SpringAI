package com.javatechie.rag;


import com.javatechie.entity.SupportQuery;
import com.javatechie.repo.SupportQueryRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;

import java.util.ArrayList;
import java.util.List;

public class RagDBDataLoader {
    private final VectorStore vectorStore;

    private final SupportQueryRepository supportQueryRepository;

    public RagDBDataLoader(VectorStore vectorStore, SupportQueryRepository supportQueryRepository) {
        this.vectorStore = vectorStore;
        this.supportQueryRepository = supportQueryRepository;
    }


    @PostConstruct
    public void loadPdfDataToVectorStore() {

        // read data from database
        // create one customer support DB (question-answer)

        /*List<SupportQuery> queries = supportQueryRepository.findAll();
        List<Document> documents = new ArrayList<>();
        for(SupportQuery query : queries){
            Document document = new Document("Question : "+query.getQuestion() + " And Answer : "+query.getAnswer());
            documents.add(document);
        }*/
        List<Document> documents = supportQueryRepository.findAll()
                .stream()
                .map(query -> new Document(
                        "Question : " + query.getQuestion() +
                                " And Answer : " + query.getAnswer()))
                .toList();

        TextSplitter textSplitter = TokenTextSplitter.builder()
                .withChunkSize(20)
                .withMaxNumChunks(200)
                .build();

        List<Document> documentChunks = textSplitter.split(documents);
        vectorStore.add(documentChunks);
    }
}

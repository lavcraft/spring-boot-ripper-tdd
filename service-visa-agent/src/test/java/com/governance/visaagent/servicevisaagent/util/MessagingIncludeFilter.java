package com.governance.visaagent.servicevisaagent.util;

import org.jetbrains.annotations.NotNull;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.core.annotation.MergedAnnotation;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.stereotype.Service;

import java.io.IOException;

public class MessagingIncludeFilter implements TypeFilter {

    @Override
    public boolean match(MetadataReader metadataReader,
                         @NotNull MetadataReaderFactory metadataReaderFactory) throws IOException
    {
        String className = metadataReader.getClassMetadata().getClassName();
        MergedAnnotation<Service> serviceMergedAnnotation = metadataReader.getAnnotationMetadata()
                                                                          .getAnnotations()
                                                                          .get(Service.class);

        return className.contains("Messaging") ||
       (className.contains("Service") && serviceMergedAnnotation.isPresent());
    }
}

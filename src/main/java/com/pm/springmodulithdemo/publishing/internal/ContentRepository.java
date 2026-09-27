
package com.pm.springmodulithdemo.publishing.internal;

import com.pm.springmodulithdemo.publishing.Content;
import org.springframework.data.repository.ListCrudRepository;

public interface ContentRepository extends ListCrudRepository<Content, Long> {
}

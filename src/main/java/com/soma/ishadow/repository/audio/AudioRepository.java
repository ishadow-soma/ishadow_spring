package com.soma.ishadow.repository.audio;

import com.soma.ishadow.domains.audio.Audio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AudioRepository extends JpaRepository<Audio, Long> , AudioRepositoryQuerydsl {

    Audio save(Audio audio);
}

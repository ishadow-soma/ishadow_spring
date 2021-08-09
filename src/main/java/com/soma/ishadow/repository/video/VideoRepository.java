package com.soma.ishadow.repository.video;

import com.soma.ishadow.domains.audio.Audio;
import com.soma.ishadow.domains.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface VideoRepository extends JpaRepository<Audio, Long> {
}

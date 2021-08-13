package com.soma.ishadow;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JsonParseTest {

    @Test
    @DisplayName("JsonParseTest")
    public void JsonParseTest() {
        String str = "{\"results\": [{\"transcript\": \"what's what's he like the I got the impression women do crazy for Simon Cowell am I kind of crazy when I the reason I like the way that I met Simon was they asked me to be a guest judge focused Conan to be on fire\", \"confidence\": 0.8996278643608093, \"speaker_tag\": 1, \"words\": [{\"word\": \"what's\", \"start_time\": \"0:00:00.200000\", \"end_time\": \"0:00:00.700000\"}, {\"word\": \"what's\", \"start_time\": \"0:00:00.700000\", \"end_time\": \"0:00:01.400000\"}, {\"word\": \"he\", \"start_time\": \"0:00:01.400000\", \"end_time\": \"0:00:01.500000\"}, {\"word\": \"like\", \"start_time\": \"0:00:01.500000\", \"end_time\": \"0:00:01.800000\"}, {\"word\": \"the\", \"start_time\": \"0:00:01.800000\", \"end_time\": \"0:00:02\"}, {\"word\": \"I\", \"start_time\": \"0:00:02\", \"end_time\": \"0:00:02.100000\"}, {\"word\": \"got\", \"start_time\": \"0:00:02.100000\", \"end_time\": \"0:00:02.300000\"}, {\"word\": \"the\", \"start_time\": \"0:00:02.300000\", \"end_time\": \"0:00:02.300000\"}, {\"word\": \"impression\", \"start_time\": \"0:00:02.300000\", \"end_time\": \"0:00:02.400000\"}, {\"word\": \"women\", \"start_time\": \"0:00:02.400000\", \"end_time\": \"0:00:03\"}, {\"word\": \"do\", \"start_time\": \"0:00:03\", \"end_time\": \"0:00:03.100000\"}, {\"word\": \"crazy\", \"start_time\": \"0:00:03.100000\", \"end_time\": \"0:00:03.400000\"}, {\"word\": \"for\", \"start_time\": \"0:00:03.400000\", \"end_time\": \"0:00:03.600000\"}, {\"word\": \"Simon\", \"start_time\": \"0:00:03.600000\", \"end_time\": \"0:00:03.900000\"}, {\"word\": \"Cowell\", \"start_time\": \"0:00:03.900000\", \"end_time\": \"0:00:04.400000\"}, {\"word\": \"am\", \"start_time\": \"0:00:04.400000\", \"end_time\": \"0:00:04.500000\"}, {\"word\": \"I\", \"start_time\": \"0:00:04.500000\", \"end_time\": \"0:00:04.600000\"}, {\"word\": \"kind\", \"start_time\": \"0:00:04.600000\", \"end_time\": \"0:00:05.300000\"}, {\"word\": \"of\", \"start_time\": \"0:00:05.300000\", \"end_time\": \"0:00:05.500000\"}, {\"word\": \"crazy\", \"start_time\": \"0:00:05.500000\", \"end_time\": \"0:00:06\"}, {\"word\": \"when\", \"start_time\": \"0:00:06\", \"end_time\": \"0:00:06.300000\"}, {\"word\": \"I\", \"start_time\": \"0:00:06.300000\", \"end_time\": \"0:00:06.400000\"}, {\"word\": \"the\", \"start_time\": \"0:00:06.400000\", \"end_time\": \"0:00:06.600000\"}, {\"word\": \"reason\", \"start_time\": \"0:00:06.600000\", \"end_time\": \"0:00:06.800000\"}, {\"word\": \"I\", \"start_time\": \"0:00:06.800000\", \"end_time\": \"0:00:07\"}, {\"word\": \"like\", \"start_time\": \"0:00:07\", \"end_time\": \"0:00:07.300000\"}, {\"word\": \"the\", \"start_time\": \"0:00:07.300000\", \"end_time\": \"0:00:07.300000\"}, {\"word\": \"way\", \"start_time\": \"0:00:07.300000\", \"end_time\": \"0:00:07.600000\"}, {\"word\": \"that\", \"start_time\": \"0:00:07.600000\", \"end_time\": \"0:00:07.600000\"}, {\"word\": \"I\", \"start_time\": \"0:00:07.600000\", \"end_time\": \"0:00:07.900000\"}, {\"word\": \"met\", \"start_time\": \"0:00:07.900000\", \"end_time\": \"0:00:08.300000\"}, {\"word\": \"Simon\", \"start_time\": \"0:00:08.300000\", \"end_time\": \"0:00:08.500000\"}, {\"word\": \"was\", \"start_time\": \"0:00:08.500000\", \"end_time\": \"0:00:09.100000\"}, {\"word\": \"they\", \"start_time\": \"0:00:09.100000\", \"end_time\": \"0:00:09.300000\"}, {\"word\": \"asked\", \"start_time\": \"0:00:09.300000\", \"end_time\": \"0:00:09.400000\"}, {\"word\": \"me\", \"start_time\": \"0:00:09.400000\", \"end_time\": \"0:00:09.600000\"}, {\"word\": \"to\", \"start_time\": \"0:00:09.600000\", \"end_time\": \"0:00:09.600000\"}, {\"word\": \"be\", \"start_time\": \"0:00:09.600000\", \"end_time\": \"0:00:09.800000\"}, {\"word\": \"a\", \"start_time\": \"0:00:09.800000\", \"end_time\": \"0:00:09.800000\"}, {\"word\": \"guest\", \"start_time\": \"0:00:09.800000\", \"end_time\": \"0:00:10.200000\"}, {\"word\": \"judge\", \"start_time\": \"0:00:10.200000\", \"end_time\": \"0:00:10.500000\"}, {\"word\": \"focused\", \"start_time\": \"0:00:10.500000\", \"end_time\": \"0:00:11.200000\"}, {\"word\": \"Conan\", \"start_time\": \"0:00:11.200000\", \"end_time\": \"0:00:11.800000\"}, {\"word\": \"to\", \"start_time\": \"0:00:11.800000\", \"end_time\": \"0:00:12.400000\"}, {\"word\": \"be\", \"start_time\": \"0:00:12.400000\", \"end_time\": \"0:00:12.600000\"}, {\"word\": \"on\", \"start_time\": \"0:00:12.600000\", \"end_time\": \"0:00:13\"}, {\"word\": \"fire\", \"start_time\": \"0:00:13\", \"end_time\": \"0:00:25.500000\"}]}, {\"transcript\": \"I am sorry North\", \"confidence\": 0.8014473915100098, \"speaker_tag\": 2, \"words\": [{\"word\": \"I\", \"start_time\": \"0:00:31.700000\", \"end_time\": \"0:00:31.900000\"}, {\"word\": \"am\", \"start_time\": \"0:00:31.900000\", \"end_time\": \"0:00:32.100000\"}, {\"word\": \"sorry\", \"start_time\": \"0:00:32.100000\", \"end_time\": \"0:00:32.500000\"}, {\"word\": \"North\", \"start_time\": \"0:00:32.500000\", \"end_time\": \"0:00:34.300000\"}]}, {\"transcript\": \" Barbie with be real here for a second\", \"confidence\": 0.8155207633972168, \"speaker_tag\": 2, \"words\": [{\"word\": \"Barbie\", \"start_time\": \"0:00:43.800000\", \"end_time\": \"0:00:44.700000\"}, {\"word\": \"with\", \"start_time\": \"0:00:44.700000\", \"end_time\": \"0:00:44.900000\"}, {\"word\": \"be\", \"start_time\": \"0:00:44.900000\", \"end_time\": \"0:00:45.200000\"}, {\"word\": \"real\", \"start_time\": \"0:00:45.200000\", \"end_time\": \"0:00:45.600000\"}, {\"word\": \"here\", \"start_time\": \"0:00:45.600000\", \"end_time\": \"0:00:45.800000\"}, {\"word\": \"for\", \"start_time\": \"0:00:45.800000\", \"end_time\": \"0:00:46.100000\"}, {\"word\": \"a\", \"start_time\": \"0:00:46.100000\", \"end_time\": \"0:00:46.200000\"}, {\"word\": \"second\", \"start_time\": \"0:00:46.200000\", \"end_time\": \"0:00:46.800000\"}]}, {\"transcript\": \" I'm going to look down there\", \"confidence\": 0.9466943740844727, \"speaker_tag\": 2, \"words\": [{\"word\": \"I'm\", \"start_time\": \"0:00:48.400000\", \"end_time\": \"0:00:48.600000\"}, {\"word\": \"going\", \"start_time\": \"0:00:48.600000\", \"end_time\": \"0:00:48.800000\"}, {\"word\": \"to\", \"start_time\": \"0:00:48.800000\", \"end_time\": \"0:00:48.800000\"}, {\"word\": \"look\", \"start_time\": \"0:00:48.800000\", \"end_time\": \"0:00:49\"}, {\"word\": \"down\", \"start_time\": \"0:00:49\", \"end_time\": \"0:00:49.300000\"}, {\"word\": \"there\", \"start_time\": \"0:00:49.300000\", \"end_time\": \"0:00:49.400000\"}]}, {\"transcript\": \" hi, I just threw my neck out\", \"confidence\": 0.9446247816085815, \"speaker_tag\": 2, \"words\": [{\"word\": \"hi,\", \"start_time\": \"0:01:07.300000\", \"end_time\": \"0:01:08.500000\"}, {\"word\": \"I\", \"start_time\": \"0:01:08.500000\", \"end_time\": \"0:01:09.600000\"}, {\"word\": \"just\", \"start_time\": \"0:01:09.600000\", \"end_time\": \"0:01:09.700000\"}, {\"word\": \"threw\", \"start_time\": \"0:01:09.700000\", \"end_time\": \"0:01:10.200000\"}, {\"word\": \"my\", \"start_time\": \"0:01:10.200000\", \"end_time\": \"0:01:10.400000\"}, {\"word\": \"neck\", \"start_time\": \"0:01:10.400000\", \"end_time\": \"0:01:10.800000\"}, {\"word\": \"out\", \"start_time\": \"0:01:10.800000\", \"end_time\": \"0:01:10.900000\"}]}, {\"transcript\": \" anyway you were saying what how is Zone will have his own where I'm up here actually crazy for for Simon's moves\", \"confidence\": 0.9187530875205994, \"speaker_tag\": 2, \"words\": [{\"word\": \"anyway\", \"start_time\": \"0:01:12.600000\", \"end_time\": \"0:01:13.200000\"}, {\"word\": \"you\", \"start_time\": \"0:01:13.200000\", \"end_time\": \"0:01:13.300000\"}, {\"word\": \"were\", \"start_time\": \"0:01:13.300000\", \"end_time\": \"0:01:13.500000\"}, {\"word\": \"saying\", \"start_time\": \"0:01:13.500000\", \"end_time\": \"0:01:13.900000\"}, {\"word\": \"what\", \"start_time\": \"0:01:13.900000\", \"end_time\": \"0:01:14\"}, {\"word\": \"how\", \"start_time\": \"0:01:14\", \"end_time\": \"0:01:14.100000\"}, {\"word\": \"is\", \"start_time\": \"0:01:14.100000\", \"end_time\": \"0:01:14.300000\"}, {\"word\": \"Zone\", \"start_time\": \"0:01:14.300000\", \"end_time\": \"0:01:14.700000\"}, {\"word\": \"will\", \"start_time\": \"0:01:14.700000\", \"end_time\": \"0:01:15.200000\"}, {\"word\": \"have\", \"start_time\": \"0:01:15.200000\", \"end_time\": \"0:01:15.300000\"}, {\"word\": \"his\", \"start_time\": \"0:01:15.300000\", \"end_time\": \"0:01:15.500000\"}, {\"word\": \"own\", \"start_time\": \"0:01:15.500000\", \"end_time\": \"0:01:15.700000\"}, {\"word\": \"where\", \"start_time\": \"0:01:15.700000\", \"end_time\": \"0:01:15.900000\"}, {\"word\": \"I'm\", \"start_time\": \"0:01:15.900000\", \"end_time\": \"0:01:16\"}, {\"word\": \"up\", \"start_time\": \"0:01:16\", \"end_time\": \"0:01:16.100000\"}, {\"word\": \"here\", \"start_time\": \"0:01:16.100000\", \"end_time\": \"0:01:16.200000\"}, {\"word\": \"actually\", \"start_time\": \"0:01:16.200000\", \"end_time\": \"0:01:19.200000\"}, {\"word\": \"crazy\", \"start_time\": \"0:01:19.200000\", \"end_time\": \"0:01:19.800000\"}, {\"word\": \"for\", \"start_time\": \"0:01:19.800000\", \"end_time\": \"0:01:20.900000\"}, {\"word\": \"for\", \"start_time\": \"0:01:20.900000\", \"end_time\": \"0:01:21.500000\"}, {\"word\": \"Simon's\", \"start_time\": \"0:01:21.500000\", \"end_time\": \"0:01:22.300000\"}, {\"word\": \"moves\", \"start_time\": \"0:01:22.300000\", \"end_time\": \"0:01:23.600000\"}]}, {\"transcript\": \" as well do Jester down here but I don't know what you're doing\", \"confidence\": 0.7882447838783264, \"speaker_tag\": 1, \"words\": [{\"word\": \"as\", \"start_time\": \"0:01:24.500000\", \"end_time\": \"0:01:24.900000\"}, {\"word\": \"well\", \"start_time\": \"0:01:24.900000\", \"end_time\": \"0:01:25.200000\"}, {\"word\": \"do\", \"start_time\": \"0:01:25.200000\", \"end_time\": \"0:01:26.400000\"}, {\"word\": \"Jester\", \"start_time\": \"0:01:26.400000\", \"end_time\": \"0:01:26.800000\"}, {\"word\": \"down\", \"start_time\": \"0:01:26.800000\", \"end_time\": \"0:01:27\"}, {\"word\": \"here\", \"start_time\": \"0:01:27\", \"end_time\": \"0:01:27.200000\"}, {\"word\": \"but\", \"start_time\": \"0:01:27.200000\", \"end_time\": \"0:01:27.300000\"}, {\"word\": \"I\", \"start_time\": \"0:01:27.300000\", \"end_time\": \"0:01:27.400000\"}, {\"word\": \"don't\", \"start_time\": \"0:01:27.400000\", \"end_time\": \"0:01:27.400000\"}, {\"word\": \"know\", \"start_time\": \"0:01:27.400000\", \"end_time\": \"0:01:27.600000\"}, {\"word\": \"what\", \"start_time\": \"0:01:27.600000\", \"end_time\": \"0:01:27.700000\"}, {\"word\": \"you're\", \"start_time\": \"0:01:27.700000\", \"end_time\": \"0:01:27.800000\"}, {\"word\": \"doing\", \"start_time\": \"0:01:27.800000\", \"end_time\": \"0:01:28.200000\"}]}, {\"transcript\": \" women actually go crazy for Simon's moves moves\", \"confidence\": 0.9350515604019165, \"speaker_tag\": 1, \"words\": [{\"word\": \"women\", \"start_time\": \"0:01:30.200000\", \"end_time\": \"0:01:30.800000\"}, {\"word\": \"actually\", \"start_time\": \"0:01:30.800000\", \"end_time\": \"0:01:31.100000\"}, {\"word\": \"go\", \"start_time\": \"0:01:31.100000\", \"end_time\": \"0:01:31.300000\"}, {\"word\": \"crazy\", \"start_time\": \"0:01:31.300000\", \"end_time\": \"0:01:31.700000\"}, {\"word\": \"for\", \"start_time\": \"0:01:31.700000\", \"end_time\": \"0:01:31.900000\"}, {\"word\": \"Simon's\", \"start_time\": \"0:01:31.900000\", \"end_time\": \"0:01:32.400000\"}, {\"word\": \"moves\", \"start_time\": \"0:01:32.400000\", \"end_time\": \"0:01:32.900000\"}, {\"word\": \"moves\", \"start_time\": \"0:01:32.900000\", \"end_time\": \"0:01:33.900000\"}]}, {\"transcript\": \" I don't know that word moved on that one and fortunately Ryan Seacrest taught it to me talk to me about Ryan Seacrest\", \"confidence\": 0.8525589108467102, \"speaker_tag\": 1, \"words\": [{\"word\": \"I\", \"start_time\": \"0:01:35\", \"end_time\": \"0:01:35.300000\"}, {\"word\": \"don't\", \"start_time\": \"0:01:35.300000\", \"end_time\": \"0:01:35.500000\"}, {\"word\": \"know\", \"start_time\": \"0:01:35.500000\", \"end_time\": \"0:01:35.600000\"}, {\"word\": \"that\", \"start_time\": \"0:01:35.600000\", \"end_time\": \"0:01:35.700000\"}, {\"word\": \"word\", \"start_time\": \"0:01:35.700000\", \"end_time\": \"0:01:35.900000\"}, {\"word\": \"moved\", \"start_time\": \"0:01:35.900000\", \"end_time\": \"0:01:36.300000\"}, {\"word\": \"on\", \"start_time\": \"0:01:36.300000\", \"end_time\": \"0:01:36.400000\"}, {\"word\": \"that\", \"start_time\": \"0:01:36.400000\", \"end_time\": \"0:01:36.600000\"}, {\"word\": \"one\", \"start_time\": \"0:01:36.600000\", \"end_time\": \"0:01:36.800000\"}, {\"word\": \"and\", \"start_time\": \"0:01:36.800000\", \"end_time\": \"0:01:36.900000\"}, {\"word\": \"fortunately\", \"start_time\": \"0:01:36.900000\", \"end_time\": \"0:01:37.300000\"}, {\"word\": \"Ryan\", \"start_time\": \"0:01:37.300000\", \"end_time\": \"0:01:37.700000\"}, {\"word\": \"Seacrest\", \"start_time\": \"0:01:37.700000\", \"end_time\": \"0:01:38.100000\"}, {\"word\": \"taught\", \"start_time\": \"0:01:38.100000\", \"end_time\": \"0:01:38.200000\"}, {\"word\": \"it\", \"start_time\": \"0:01:38.200000\", \"end_time\": \"0:01:38.300000\"}, {\"word\": \"to\", \"start_time\": \"0:01:38.300000\", \"end_time\": \"0:01:38.500000\"}, {\"word\": \"me\", \"start_time\": \"0:01:38.500000\", \"end_time\": \"0:01:38.600000\"}, {\"word\": \"talk\", \"start_time\": \"0:01:38.600000\", \"end_time\": \"0:01:39.200000\"}, {\"word\": \"to\", \"start_time\": \"0:01:39.200000\", \"end_time\": \"0:01:39.300000\"}, {\"word\": \"me\", \"start_time\": \"0:01:39.300000\", \"end_time\": \"0:01:39.400000\"}, {\"word\": \"about\", \"start_time\": \"0:01:39.400000\", \"end_time\": \"0:01:39.500000\"}, {\"word\": \"Ryan\", \"start_time\": \"0:01:39.500000\", \"end_time\": \"0:01:41.300000\"}, {\"word\": \"Seacrest\", \"start_time\": \"0:01:41.300000\", \"end_time\": \"0:01:41.400000\"}]}, {\"transcript\": \" Enderman a word Ryan doesn't have like the whole estrogen and soy milk to make it down there but now you're rubbing them\", \"confidence\": 0.9514108896255493, \"speaker_tag\": 2, \"words\": [{\"word\": \"Enderman\", \"start_time\": \"0:01:42.500000\", \"end_time\": \"0:01:43.500000\"}, {\"word\": \"a\", \"start_time\": \"0:01:43.500000\", \"end_time\": \"0:01:43.600000\"}, {\"word\": \"word\", \"start_time\": \"0:01:43.600000\", \"end_time\": \"0:01:44\"}, {\"word\": \"Ryan\", \"start_time\": \"0:01:44\", \"end_time\": \"0:01:44.400000\"}, {\"word\": \"doesn't\", \"start_time\": \"0:01:44.400000\", \"end_time\": \"0:01:49.800000\"}, {\"word\": \"have\", \"start_time\": \"0:01:49.800000\", \"end_time\": \"0:01:50.100000\"}, {\"word\": \"like\", \"start_time\": \"0:01:50.100000\", \"end_time\": \"0:01:50.400000\"}, {\"word\": \"the\", \"start_time\": \"0:01:50.400000\", \"end_time\": \"0:01:50.500000\"}, {\"word\": \"whole\", \"start_time\": \"0:01:50.500000\", \"end_time\": \"0:01:50.700000\"}, {\"word\": \"estrogen\", \"start_time\": \"0:01:50.700000\", \"end_time\": \"0:01:52.200000\"}, {\"word\": \"and\", \"start_time\": \"0:01:52.200000\", \"end_time\": \"0:01:52.200000\"}, {\"word\": \"soy\", \"start_time\": \"0:01:52.200000\", \"end_time\": \"0:01:52.400000\"}, {\"word\": \"milk\", \"start_time\": \"0:01:52.400000\", \"end_time\": \"0:01:52.600000\"}, {\"word\": \"to\", \"start_time\": \"0:01:52.600000\", \"end_time\": \"0:01:52.900000\"}, {\"word\": \"make\", \"start_time\": \"0:01:52.900000\", \"end_time\": \"0:01:55.900000\"}, {\"word\": \"it\", \"start_time\": \"0:01:55.900000\", \"end_time\": \"0:01:56\"}, {\"word\": \"down\", \"start_time\": \"0:01:56\", \"end_time\": \"0:01:56.200000\"}, {\"word\": \"there\", \"start_time\": \"0:01:56.200000\", \"end_time\": \"0:01:56.300000\"}, {\"word\": \"but\", \"start_time\": \"0:01:56.300000\", \"end_time\": \"0:01:56.500000\"}, {\"word\": \"now\", \"start_time\": \"0:01:56.500000\", \"end_time\": \"0:01:56.700000\"}, {\"word\": \"you're\", \"start_time\": \"0:01:56.700000\", \"end_time\": \"0:01:56.900000\"}, {\"word\": \"rubbing\", \"start_time\": \"0:01:56.900000\", \"end_time\": \"0:01:57.400000\"}, {\"word\": \"them\", \"start_time\": \"0:01:57.400000\", \"end_time\": \"0:01:57.500000\"}]}, {\"transcript\": \" they're very there they're strong They are perky and ensure they're alive and alert for us speak to you streaming\", \"confidence\": 0.871834397315979, \"speaker_tag\": 1, \"words\": [{\"word\": \"they're\", \"start_time\": \"0:02:03.700000\", \"end_time\": \"0:02:04.100000\"}, {\"word\": \"very\", \"start_time\": \"0:02:04.100000\", \"end_time\": \"0:02:04.400000\"}, {\"word\": \"there\", \"start_time\": \"0:02:04.400000\", \"end_time\": \"0:02:04.800000\"}, {\"word\": \"they're\", \"start_time\": \"0:02:04.800000\", \"end_time\": \"0:02:05.100000\"}, {\"word\": \"strong\", \"start_time\": \"0:02:05.100000\", \"end_time\": \"0:02:05.500000\"}, {\"word\": \"They\", \"start_time\": \"0:02:05.500000\", \"end_time\": \"0:02:05.600000\"}, {\"word\": \"are\", \"start_time\": \"0:02:05.600000\", \"end_time\": \"0:02:05.700000\"}, {\"word\": \"perky\", \"start_time\": \"0:02:05.700000\", \"end_time\": \"0:02:06.100000\"}, {\"word\": \"and\", \"start_time\": \"0:02:06.100000\", \"end_time\": \"0:02:06.400000\"}, {\"word\": \"ensure\", \"start_time\": \"0:02:06.400000\", \"end_time\": \"0:02:07.700000\"}, {\"word\": \"they're\", \"start_time\": \"0:02:07.700000\", \"end_time\": \"0:02:10.900000\"}, {\"word\": \"alive\", \"start_time\": \"0:02:10.900000\", \"end_time\": \"0:02:12.600000\"}, {\"word\": \"and\", \"start_time\": \"0:02:12.600000\", \"end_time\": \"0:02:12.700000\"}, {\"word\": \"alert\", \"start_time\": \"0:02:12.700000\", \"end_time\": \"0:02:13\"}, {\"word\": \"for\", \"start_time\": \"0:02:13\", \"end_time\": \"0:02:13.400000\"}, {\"word\": \"us\", \"start_time\": \"0:02:13.400000\", \"end_time\": \"0:02:13.600000\"}, {\"word\": \"speak\", \"start_time\": \"0:02:13.600000\", \"end_time\": \"0:02:16\"}, {\"word\": \"to\", \"start_time\": \"0:02:16\", \"end_time\": \"0:02:16.200000\"}, {\"word\": \"you\", \"start_time\": \"0:02:16.200000\", \"end_time\": \"0:02:16.300000\"}, {\"word\": \"streaming\", \"start_time\": \"0:02:16.300000\", \"end_time\": \"0:02:18.700000\"}]}]}";
        JsonElement element = JsonParser.parseString(str);
        JsonArray contexts = element.getAsJsonObject().get("results").getAsJsonArray();
        int jsonSize = element.getAsJsonObject().get("results").getAsJsonArray().size();
        StringBuilder sb = new StringBuilder();
        for( int i = 0; i < jsonSize; i++) {
            JsonElement context = contexts.get(i);
            String transcript = context.getAsJsonObject().get("transcript").getAsString();
            String confidence = context.getAsJsonObject().get("confidence").getAsString();
            String speakerTag = context.getAsJsonObject().get("speaker_tag").getAsString();
            JsonArray words = context.getAsJsonObject().get("words").getAsJsonArray();
            int wordSize = context.getAsJsonObject().get("words").getAsJsonArray().size();
            String startTime = words.get(0).getAsJsonObject().get("start_time").getAsString();
            if(startTime.length() < 8) {
                startTime += ".000000";
            }
            String endTime = words.get(wordSize - 1).getAsJsonObject().get("start_time").getAsString();
            if(endTime.length() < 8) {
                endTime += ".000000";
            }
            sb.append(i + 1).append("\n").append(transcript).append("\n").append(confidence).append("\n")
                    .append(speakerTag).append("\n").append(startTime).append("\n")
                    .append(endTime).append("\n").append("\n");
        }
        System.out.println(sb);

    }
}

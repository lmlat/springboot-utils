package com.github.akor.enums;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * MimeType类型枚举
 * http://blog.sina.com.cn/s/blog_5c0175790100bd1j.html
 * @Company QAX
 * @Author : ai.tao
 * @Create : 2022/2/23 15:52
 */
public enum MimeType {
    acad("AutoCAD-Dateien (nach NCSA)") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("dwg");
        }
    },
    astound("Astound-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("asd", "asn");
        }
    },
    dsptype("Astound-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("tsp");
        }
    },
    dxf("AutoCAD-Dateien (nach CERN)") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("dxf");
        }
    },
    futuresplash("Flash Futuresplash-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("spl");
        }
    },
    gzip("GNU Zip-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("gz");
        }
    },
    listenup("Listenup-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("ptlk");
        }
    },
    mac_binhex40("Macintosh Bin?r-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("hqx");
        }
    },
    mbedlet("Mbedlet-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("mbd");
        }
    },
    mif("FrameMaker Interchange Format Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("mif");
        }
    },
    msexcel("Microsoft Excel Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("xls", "xla");
        }
    },
    mshelp("Microsoft Windows Hilfe Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("hlp", "chm");
        }
    },
    mspowerpoint("Microsoft Powerpoint Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("ppt", "ppz", "pps", "pot");
        }
    },
    msword("Microsoft Word Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("doc", "dot");
        }
    },
    octect_stream("Ausführbare Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("bin", "exe", "com", "dll", "class");
        }
    },
    oda("Oda-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("oda");
        }
    },
    pdf("Adobe PDF-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("pdf");
        }
    },
    postscript("Adobe Postscript-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("ai", "eps", "ps");
        }
    },
    rtc("RTC-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("rtc");
        }
    },
    rtf("Microsoft RTF-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("rtf");
        }
    },
    studiom("Studiom-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("smp");
        }
    },
    toolbook("Toolbook-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("tbk");
        }
    },
    vocaltec_media_desc("Vocaltec Mediadesc-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("vmd");
        }
    },
    vocaltec_media_file("Vocaltec Media-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("vmf");
        }
    },

    x_speech("Speech-Dateien") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("talk", "spc");
        }
    },
    x_sgml("SGML-Dateien") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("sgm", "sgml");
        }
    },
    x_setext("SeText-Dateien") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("etx");
        }
    },
    vnd_wap_wmlscriptc("WML-Scriptdateien (WAP)") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("wmlsc");
        }
    },
    vnd_wap_wmlc("WMLC-Dateien (WAP)") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("wmls");
        }
    },
    vnd_wap_wml("WML-Dateien (WAP)") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("wml");
        }
    },
    tab_separated_values("tabulator-separierte Datendateien") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("tsv");
        }
    },
    richtext("Richtext-Dateien") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("rtx");
        }
    },
    plain("reine Textdateien") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("txt");
        }
    },
    javascript("JavaScript-Dateien") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("js");
        }
    },
    x_javascript("serverseitige JavaScript-Dateien") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("js");
        }
    },
    html("-Dateien") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("html");
        }
    },
    css("CSS Stylesheet-Dateien") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("css");
        }
    },
    comma_separated_values("komma-separierte Datendateien") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("csv");
        }
    },
    vrml("Visualisierung virtueller Welten") {
        @Override
        public String getName() {
            return model();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("wrl");
        }
    },
    x_xpixmap("XPM-Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("xpm");
        }
    },
    x_xbitmap("XBM-Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("xbm");
        }
    },
    x_windowdump("X-Windows Dump") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("xwd");
        }
    },
    x_rgb("RGB-Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("rgb");
        }
    },
    x_portable_pixmap("PBM Pixmap Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("ppm");
        }
    },
    x_portable_graymap("PBM Graymap Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("pgm");
        }
    },
    x_portable_bitmap("PBM Bitmap Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("pbm");
        }
    },
    x_portable_anymap("PBM Anymap Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("pnm");
        }
    },
    x_freehand("Freehand-Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("fh4", "fh5", "fhc");
        }
    },
    vnd_wap_wbmp("Bitmap-Dateien (WAP)") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("wbmp");
        }
    },
    vasa("Vasa-Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("mcf");
        }
    },
    tiff("TIFF-Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("tiff", "tif");
        }
    },
    jpeg("JPEG-Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("jpeg", "jpg", "jpe");
        }
    },
    ief("IEF-Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("ief");
        }
    },
    gif("GIF-Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("gif");
        }
    },
    fif("FIF-Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("fif");
        }
    },
    cmu_raster("CMU-Raster-Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("ras");
        }
    },
    cis_cod("CIS-Cod-Dateien") {
        @Override
        public String getName() {
            return image();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("cod");
        }
    },
    xdwf("Drawing-Dateien") {
        @Override
        public String getName() {
            return drawing();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("dwf");
        }
    },
    xwav("Wav-Dateien") {
        @Override
        public String getName() {
            return audio();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("wav");
        }
    },
    xqt_stream("-Dateien") {
        @Override
        public String getName() {
            return audio();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("stream");
        }
    },
    xpn_realaudio_plugin("RealAudio-Plugin-Dateien") {
        @Override
        public String getName() {
            return audio();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("rpm");
        }
    },
    x_pn_realaudio("RealAudio-Dateien") {
        @Override
        public String getName() {
            return audio();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("ram", "ra");
        }
    },
    x_mpeg("MPEG-Dateien") {
        @Override
        public String getName() {
            return video();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("mpeg");
        }
    },
    x_midi("MIDI-Dateien") {
        @Override
        public String getName() {
            return video();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("mid", "midi");
        }
    },
    x_dspeeh("Sprachdateien") {
        @Override
        public String getName() {
            return video();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("dus", "cht");
        }
    },
    x_aiff("AIFF-Sound-Dateien") {
        @Override
        public String getName() {
            return video();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("aif", "aiff", "aifc");
        }
    },
    voxware("Vox-Dateien") {
        @Override
        public String getName() {
            return video();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("vox");
        }
    },
    tsplayer("TS-Player-Dateien") {
        @Override
        public String getName() {
            return video();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("tsi");
        }
    },
    echospeech("Echospeed-Dateien") {
        @Override
        public String getName() {
            return audio();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("es");
        }
    },
    basic("Sound-Dateien") {
        @Override
        public String getName() {
            return audio();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("au", "snd");
        }
    },
    zip("ZIP-Archivdateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("zip");
        }
    },
    x_wais_source("WAIS Quelldateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("src");
        }
    },
    x_ustar("tar-Archivdateien (Posix)") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("ustar");
        }
    },
    x_troff_ms("TROFF-Dateien mit MS-Makros (Unix)") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("me", "troff");
        }
    },
    x_troff_me("TROFF-Dateien mit ME-Makros (Unix)") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("me", "troff");
        }
    },
    x_troff_man("TROFF-Dateien mit MAN-Makros (Unix)") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("man", "troff");
        }
    },
    x_troff("TROFF-Dateien (Unix)") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("t", "tr", "roff");
        }
    },
    x_texinfo("TEXinfo-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("texinfo", "texi");
        }
    },
    x_tex("TEX-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("tex");
        }
    },
    x_tcl("TCL Scriptdateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("tcl");
        }
    },
    x_tar("tar-Archivdateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("tar");
        }
    },
    x_sv4crc("CPIO-Dateien mit CRC") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("sv4crc");
        }
    },
    x_sv4cpio("CPIO-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("sv4cpio");
        }
    },
    x_supercard("Supercard-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("sca");
        }
    },
    x_stuffit("Stuffit-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("sit");
        }
    },
    x_sprite("Sprite-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("spr", "sprite");
        }
    },
    x_shockwave_flash("Flash Shockwave-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("swf", "cab");
        }
    },
    x_shar("Shell-Archiv-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("schar");
        }
    },
    x_sh("Bourne Shellscript-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("sh");
        }
    },
    x_nschat("NS Chat-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("nsc");
        }
    },
    x_netcdf("Unidata CDF-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("nc", "cdf");
        }
    },
    x_mif("FrameMaker Interchange Format Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("mif");
        }
    },
    x_macbinary("Macintosh Bin?rdateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("bin");
        }
    },
    x_latex("Latex-Quelldateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("latex");
        }
    },
    x_httpd_php("PHP-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("php", "phtml");
        }
    },
    x_hdf("HDF-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("hdf");
        }
    },
    x_gtar("GNU tar-Archiv-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("gtar");
        }
    },
    x_envoy("Envoy-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("evy");
        }
    },
    x_dvi("DVI-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("dvi");
        }
    },
    x_director("-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("dcr", "dir", "dxr");
        }
    },
    x_csh("C-Shellscript-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("csh");
        }
    },
    x_cpio("CPIO-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("cpio");
        }
    },
    x_compress("-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("z");
        }
    },
    x_bcpio("BCPIO-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("bcpio");
        }
    },

    mpeg("MPEG-Dateien") {
        @Override
        public String getName() {
            return application();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("mpeg", "mpg", "mpe");
        }
    },
    quicktime("Quicktime-Dateien") {
        @Override
        public String getName() {
            return video();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("qt", "mov");
        }
    },
    vndvivo("Vivo-Dateien") {
        @Override
        public String getName() {
            return video();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("viv", "vivo");
        }
    },
    x_msvideo("Microsoft AVI-Dateien") {
        @Override
        public String getName() {
            return video();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("avi");
        }
    },
    x_sgimovie("MMovie-Dateien") {
        @Override
        public String getName() {
            return video();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("movie");
        }
    },
    formulaone("FormulaOne-Dateien") {
        @Override
        public String getName() {
            return workbook();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("vts", "vtts");
        }
    },
    x_3dmf("3DMF-Dateien") {
        @Override
        public String getName() {
            return xworld();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("3dmf", "3dm", "qd3d", "qd3");
        }
    },
    properties("properties file") {
        @Override
        public String getName() {
            return text();
        }

        @Override
        public Set<String> getExtSet() {
            return toSet("properties");
        }
    },
    unknown("unknown") {
        @Override
        public String getName() {
            return "unknown";
        }

        @Override
        public Set<String> getExtSet() {
            return null;
        }
    };

    String desc;

    MimeType(String desc) {
        this.desc = desc;
    }

    public static MimeType getInstance(String name) {
        for (MimeType mimeType : MimeType.values()) {
            if (mimeType.getName().equalsIgnoreCase(name)) {
                return mimeType;
            }
        }
        return unknown;
    }

    public abstract String getName();

    public abstract Set<String> getExtSet();

    String application() {
        return String.join("/", "application", format());
    }

    String audio() {
        return String.join("/", "audio", format());
    }

    String drawing() {
        return String.join("/", "drawing", format());
    }

    String image() {
        return String.join("/", "image", format());
    }

    String model() {
        return String.join("/", "model", format());
    }

    String text() {
        return String.join("/", "text", format());
    }

    String video() {
        return String.join("/", "video", format());
    }

    String workbook() {
        return String.join("/", "workbook", format());
    }

    String xworld() {
        return String.join("/", "xworld", format());
    }

    Set<String> toSet(String... params) {
        return Stream.of(params).collect(Collectors.toSet());
    }

    String format() {
        String name = this.name();
        if (name.equalsIgnoreCase("properties")) {
            name = "x-java-" + name;
        }
        if (name.startsWith("vnd")) {
            name = name.replaceAll("_", ".");
        }

        if (name.startsWith("x_")) {
            name = name.replaceAll("_", "-");
        }
        return name;
    }
}

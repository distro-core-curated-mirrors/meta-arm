SUMMARY = "test"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

SRC_URI = "file://chkfeat.s file://gcs_enabled.c"

S = "${WORKDIR}"

LDFLAGS:append = " -z experimental-gcs=always"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} ${WORKDIR}/gcs_enabled.c ${WORKDIR}/chkfeat.s -o gcs-test 
}

do_install() {
    install -d ${D}${bindir}
    install -m 0755 gcs-test ${D}${bindir}
}

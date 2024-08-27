require ${COREBASE}/meta/recipes-kernel/linux-libc-headers/linux-libc-headers.inc

FILESEXTRAPATHS:prepend = "${THISDIR}/../linux/files/:"

KBRANCH = "v6.11/standard/base"
# 6.11-rc3
SRCREV = "7c626ce4bae1ac14f60076d00eafe71af30450ba"

SRC_URI = "git://git.yoctoproject.org/linux-yocto-dev.git;branch=${KBRANCH};protocol=https"
SRC_URI += "file://gcs.patch"

LIC_FILES_CHKSUM = "file://COPYING;md5=6bc538ed5bd9a7fc9398086aedcd7e46"

S = "${WORKDIR}/git"

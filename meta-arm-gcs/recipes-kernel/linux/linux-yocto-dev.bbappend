FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

KBRANCH = "v6.8/base"
SRCREV_machine = "e8f897f4afef0031fe618a8e94127a0934896aba"
SRCREV_meta = "69506f439abc9bde9dae104e53c597ed472b5940"
LINUX_VERSION = "6.8.0"

SRC_URI += "file://gcs.patch"

# TMPDIR references in:
#  /usr/src/debug/linux-yocto-dev/6.8.0+git/drivers/tty/vt/consolemap_deftbl.c
#  /usr/src/debug/linux-yocto-dev/6.8.0+git/lib/oid_registry_data.c
INSANE_SKIP:${PN}-src += "buildpaths"

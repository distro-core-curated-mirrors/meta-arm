FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

KBRANCH = "v6.10/base"
SRCREV_machine = "83a7eefedc9b56fe7bfeff13b6c7356688ffa670"
SRCREV_meta = "66aec68f0ba1d15ba0e9c19f1ec0d2b4a75c5333"
LINUX_VERSION = "6.10.0"

SRC_URI += "file://gcs.patch"

# TMPDIR references in:
#  /usr/src/debug/linux-yocto-dev/6.8.0+git/drivers/tty/vt/consolemap_deftbl.c
#  /usr/src/debug/linux-yocto-dev/6.8.0+git/lib/oid_registry_data.c
INSANE_SKIP:${PN}-src += "buildpaths"

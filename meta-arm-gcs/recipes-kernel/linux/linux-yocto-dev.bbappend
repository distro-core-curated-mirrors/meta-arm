FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

KBRANCH = "v6.11/standard/base"
SRCREV_machine = "7c626ce4bae1ac14f60076d00eafe71af30450ba"
SRCREV_meta = "66aec68f0ba1d15ba0e9c19f1ec0d2b4a75c5333"
LINUX_VERSION = "6.11.0"

SRC_URI += "file://gcs.patch file://disable_uprobe.cfg"

# TMPDIR references in:
#  /usr/src/debug/linux-yocto-dev/6.8.0+git/drivers/tty/vt/consolemap_deftbl.c
#  /usr/src/debug/linux-yocto-dev/6.8.0+git/lib/oid_registry_data.c
INSANE_SKIP:${PN}-src += "buildpaths"

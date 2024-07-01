SUMMARY = "kselftest, the kernel test suite"
SECTION = "kernel"
LICENSE = "GPL-2.0-only"

DEPENDS = "rsync-native alsa-lib libcap libcap-ng numactl popt libmnl"

inherit kernelsrc kernel-arch

PACKAGE_ARCH = "${MACHINE_ARCH}"

B = "${WORKDIR}/build"

do_compile[cleandirs] = "${B}"

CC:remove:aarch64 = "-mbranch-protection=standard"
SECURITY_CFLAGS = ""

# Disable BPF and HID as they need clang for BPF compilation
SKIP_TARGETS ?= "bpf hid"

EXTRA_OEMAKE += "\
    O=${B} \
    V=1 \
    ARCH=${ARCH} \
    CROSS_COMPILE=${TARGET_PREFIX} \
    CC="${CC}" \
    AR="${AR}" \
    LD="${LD}" \
    KHDR_INCLUDES="-isystem ${STAGING_KERNEL_BUILDDIR}" \
    SKIP_TARGETS="${SKIP_TARGETS}" \
"

# Force all tests to build successfully
#EXTRA_OEMAKE += "FORCE_TARGETS=1"

do_compile() {
	oe_runmake -C ${S}/tools/testing/selftests
}

do_install() {
	oe_runmake -C ${S}/tools/testing/selftests install INSTALL_PATH=${D}/${libexecdir}/kselftest
	# install uses rsync -a
	chown -R root:root ${D}/${libexecdir}/kselftest
}

RDEPENDS:${PN} += "bash python3-core python3 perl perl-module-io-handle"

INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INSANE_SKIP:${PN} = "already-stripped buildpaths ldflags textrel"

# TODO test with musl

require recipes-devtools/gdb/gdb-common.inc

inherit gettext pkgconfig

PACKAGES =+ "gdbserver"
FILES:gdbserver = "${bindir}/gdbserver"

require recipes-devtools/gdb/gdb.inc

FILESEXTRAPATHS:prepend := "${COREBASE}/meta/recipes-devtools/gdb/gdb:"

# This patch doesn't apply to 15.1
SRC_URI:remove = "file://0005-Change-order-of-CFLAGS.patch"
SRC_URI[sha256sum] = "38254eacd4572134bca9c5a5aa4d4ca564cbbd30c369d881f733fb6b903354f2"

inherit python3-dir

EXTRA_OEMAKE:append:libc-musl = "\
                                 gt_cv_func_gnugettext1_libc=yes \
                                 gt_cv_func_gnugettext2_libc=yes \
                                 gl_cv_func_working_strerror=yes \
                                 gl_cv_func_strerror_0_works=yes \
                                 gl_cv_func_gettimeofday_clobber=no \
                                "

do_configure:prepend() {
	if [ "${@bb.utils.filter('PACKAGECONFIG', 'python', d)}" ]; then
		cat > ${WORKDIR}/python << EOF
#!/bin/sh
case "\$2" in
	--includes) echo "-I${STAGING_INCDIR}/${PYTHON_DIR}${PYTHON_ABI}/" ;;
	--ldflags) echo "-Wl,-rpath-link,${STAGING_LIBDIR}/.. -Wl,-rpath,${libdir}/.. -lpthread -ldl -lutil -lm -lpython${PYTHON_BASEVERSION}${PYTHON_ABI}" ;;
	--exec-prefix) echo "${exec_prefix}" ;;
	*) exit 1 ;;
esac
exit 0
EOF
		chmod +x ${WORKDIR}/python
	fi
}
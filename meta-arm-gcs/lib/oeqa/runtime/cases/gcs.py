#
# SPDX-License-Identifier: MIT
#

from oeqa.runtime.case import OERuntimeTestCase
from oeqa.runtime.decorator.package import OEHasPackage

class GCSTests(OERuntimeTestCase):

    # TODO:
    # - Need a test that fails if GCS is enabled.  kselftest most likely has tests here?

    def test_dmesg(self):
        """
        Verify that the kernel detected the GCS processor feature.

        If this fails the FVP configuration or kernel is at fault.
        """
        status, output = self.target.run("dmesg | grep 'CPU features'")
        self.assertEqual(status, 0, f"dmesg failed: {output}")

        self.assertIn("CPU features: detected: Guarded Control Stack (GCS)", output)


    @OEHasPackage(['binutils'])
    def test_elf_tags(self):
        """
        Verify that ELF binaries have the GCS feature enabled.
        
        If this fails the toolchain doesn't have GCS enabled, or the compilation
        flags don't enable GCS.
        """
        status, output = self.target.run("readelf -n /bin/bash | grep 'AArch64 feature'")
        self.assertEqual(status, 0, f"readelf failed: {output}")

        self.assertIn("Properties: AArch64 feature: BTI, PAC, GCS", output)


    def test_execution(self):
        """
        Verify that enabling GCS with the glibc tunables doesn't cause a crash.

        This is an incredibly low bar, but if this fails then the GCS patches
        are at fault.
        """
        cmd = "GLIBC_TUNABLES=glibc.cpu.aarch64_gcs=1:glibc.cpu.aarch64_gcs_policy=2 ls -d /u*"
        status, output = self.target.run(cmd)
        self.assertEqual(status, 0, f"ls failed: {output}")
        self.assertEqual(output, "/usr")

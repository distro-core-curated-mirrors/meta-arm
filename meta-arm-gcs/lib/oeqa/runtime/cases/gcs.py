#
# SPDX-License-Identifier: MIT
#

import unittest

from oeqa.runtime.case import OERuntimeTestCase
from oeqa.runtime.decorator.package import OEHasPackage

class GCSTests(OERuntimeTestCase):

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

    def test_chkfeat(self):
        """
        Verify that CHKFEAT reports GCS as enabled when running with the tunables.

        If this fails then GCS is not being properly enabled at runtime glibc. The kernel
        and glibc patches may be out of sync.
        """
        cmd = "GLIBC_TUNABLES=glibc.cpu.aarch64_gcs=1:glibc.cpu.aarch64_gcs_policy=2 gcs-test"
        status, output = self.target.run(cmd)
        self.assertEqual(status, 0, f"gcs-test failed: {output}")

        # gcs-test should return 1 if GCS is not enabled via the tunables
        cmd = "gcs-test"
        status, output = self.target.run(cmd)
        self.assertEqual(status, 1, f"gcs-test failed: {output}")

    @OEHasPackage(['kselftest'])
    @unittest.expectedFailure
    def test_kselftest(self):
        """
        Run the GCS tests in the kselftest.
        """
        cmd = """
        cd /usr/libexec/kselftest; \
        ./run_kselftest.sh $(./run_kselftest.sh -l | awk -e '/arm64.*gcs/ { print "-t " $1 }') \
        """
        status, output = self.target.run(cmd)

        # kselftest normally returns 0 even if tests fail, so a failure here is serious
        self.assertEqual(status, 0, f"kselftest failed: {output}")

        # Identify any test failure lines, and assert that the list is empty
        fails = [l for l in output.splitlines() if "not ok " in l]
        self.assertFalse(fails)

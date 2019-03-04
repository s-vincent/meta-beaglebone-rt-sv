require recipes-kernel/linux/linux-yocto.inc

FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

# Skip processing of this recipe if it is not explicitly specified as the
# PREFERRED_PROVIDER for virtual/kernel. This avoids errors when trying
# to build multiple virtual/kernel providers, e.g. as dependency of
# core-image-rt-sdk, core-image-rt.
python () {
    if d.getVar("PREFERRED_PROVIDER_virtual/kernel") != "linux-yocto-ipipe":
        raise bb.parse.SkipPackage("Set PREFERRED_PROVIDER_virtual/kernel to linux-yocto-ipipe to enable it")
}

# 4.14.85
SRCREV ?= "5ff1ad556aad473952c1caca6092aac4517ac1ae"
LINUX_VERSION ?= "4.14"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-${LINUX_VERSION}.y \
           http://xenomai.org/downloads/xenomai/stable/xenomai-3.0.8.tar.bz2;name=xeno \
           file://defconfig \
           file://ipipe-core-4.14.85-arm-6.patch;apply=0 \
          "
SRC_URI[xeno.md5sum] = "eafe3b789651f0db9575599dffc60a19"
SRC_URI[xeno.sha256sum] = "c373261ddb8280d9d7078cdd9cd9646dfb7d70d8cd3aa9693d9148f03990d711"

PV = "${LINUX_VERSION}.85"

KCONF_BSP_AUDIT_LEVEL = "2"
LINUX_KERNEL_TYPE = "ipipe"

COMPATIBLE_MACHINE_beaglebone = "beaglebone"
COMPATIBLE_MACHINE_beaglebone-yocto = "beaglebone-yocto"

# Functionality flags
#KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc features/taskstats/taskstats.scc"
#KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"

do_configure_prepend() {
#  cp ${THISDIR}/${PN}/defconfig ${WORKDIR}/defconfig
}

do_prepare_kernel() {
  xenomai_src="${WORKDIR}/xenomai-3.0.8/"

  echo $xenomai_src

  ${xenomai_src}/scripts/prepare-kernel.sh --arch=arm --linux=${S} --ipipe=${WORKDIR}/ipipe-core-4.14.85-arm-6.patch
}

addtask prepare_kernel after do_patch before do_configure


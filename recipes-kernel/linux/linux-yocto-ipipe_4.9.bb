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

# 4.9.51
SRCREV ?= "089d7720383d7bc9ca6b8824a05dfa66f80d1f41"
LINUX_VERSION ?= "4.9"

SRC_URI = "git://git.kernel.org/pub/scm/linux/kernel/git/stable/linux-stable.git;branch=linux-${LINUX_VERSION}.y \
           http://xenomai.org/downloads/xenomai/stable/xenomai-3.0.6.tar.bz2;name=xeno \
           file://defconfig \
           file://ipipe-core-4.9.51-arm-3.patch;apply=0 \
          "
SRC_URI[xeno.md5sum] = "6017203d0992bb5334498c196bf6f25d"                            
SRC_URI[xeno.sha256sum] = "2c0dd3f0e36e4a10f97e0028989bb873e80f4d1ce212ac55fd3b28857c464f94"

PV = "${LINUX_VERSION}.51"

KCONF_BSP_AUDIT_LEVEL = "2"
LINUX_KERNEL_TYPE = "ipipe"

COMPATIBLE_MACHINE_beaglebone = "beaglebone"

# Functionality flags
#KERNEL_EXTRA_FEATURES ?= "features/netfilter/netfilter.scc features/taskstats/taskstats.scc"
#KERNEL_FEATURES_append = " ${KERNEL_EXTRA_FEATURES}"

do_configure_prepend() {
  cp ${THISDIR}/${PN}/defconfig ${WORKDIR}/defconfig
}

do_prepare_kernel() {
  xenomai_src="${WORKDIR}/xenomai-3.0.6/"

  echo $xenomai_src

  ${xenomai_src}/scripts/prepare-kernel.sh --arch=arm --linux=${S} --ipipe=${WORKDIR}/ipipe-core-4.9.51-arm-3.patch
}

addtask prepare_kernel after do_patch before do_configure


package monnef.core.power

object PowerValues {
  // getting ready for RedstoneFlux (after transition switch to 1)
  var corePowerCoef = 0.1f

  var corePowerGenerationCoef = 1f
  var corePowerConsumptionCoef = 1f

  var coreConfigPowerGenerationCoef = 1f
  var coreConfigPowerConsumptionCoef = 1f

  def totalPowerGenerationCoef = corePowerCoef * corePowerGenerationCoef * coreConfigPowerGenerationCoef

  def totalPowerConsumptionCoef = corePowerCoef * corePowerConsumptionCoef * coreConfigPowerConsumptionCoef
}

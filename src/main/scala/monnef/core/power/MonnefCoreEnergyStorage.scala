package monnef.core.power

import cofh.api.energy.EnergyStorage

class MonnefCoreEnergyStorage(_capacity: Int, _maxReceive: Int, _maxExtract: Int) extends EnergyStorage(_capacity, _maxReceive, _maxExtract) {
  def this(_capacity: Int, _maxTransfer: Int) = this(_capacity, _maxTransfer, _maxTransfer)

  def this(_capacity: Int) = this(_capacity, _capacity, _capacity)
}

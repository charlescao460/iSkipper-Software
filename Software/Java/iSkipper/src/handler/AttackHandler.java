package handler;

import device.ReceivedPacketEvent;

public class AttackHandler implements ReceivedPacketHandlerInterface
{

	private long attackCount;
	private boolean shouldPrintStatis;

	/**
	 * Default, print the attack count each time.
	 */
	public AttackHandler()
	{
		attackCount = 0;
		shouldPrintStatis = true;
	}

	/**
	 * @param shouldPrintStatis
	 *            Whether to print the attack count.
	 */
	public AttackHandler(boolean shouldPrintStatis)
	{
		attackCount = 0;
		this.shouldPrintStatis = shouldPrintStatis;
	}

	@Override
	public void onReceivedPacketEvent(ReceivedPacketEvent packetEvent)
	{
		int count = 0;
		for (byte b : packetEvent.getReceivedData())
		{
			if (b == '.')
				count++;
		}
		attackCount += count;
		if (shouldPrintStatis)
			System.out.format("Attack count: %d", attackCount);
	}

}

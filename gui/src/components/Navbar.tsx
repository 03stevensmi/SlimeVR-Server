import classnames from 'classnames';
import { ReactChild } from 'react';
import { NavLink, useMatch } from 'react-router-dom';
import { CubeIcon } from './commons/icon/CubeIcon';
import { GearIcon } from './commons/icon/GearIcon';

export function NavButton({
  to,
  children,
  match,
  state = {},
  icon,
}: {
  to: string;
  children: ReactChild;
  match?: string;
  state?: any;
  icon: ReactChild;
}) {
  const doesMatch = useMatch({
    path: match || to,
  });

  return (
    <NavLink
      to={to}
      state={state}
      className={classnames(
        'flex flex-col justify-center gap-4 w-[72px] xs:w-[85px] h-[85px] rounded-md group select-text',
        {
          'bg-accent-background-50 fill-accent-background-20': doesMatch,
          'hover:bg-background-70': !doesMatch,
        }
      )}
    >
      <div className="flex justify-around">
        <div
          className={classnames('xs:scale-150', {
            'fill-accent-lighter': doesMatch,
            'fill-background-50': !doesMatch,
          })}
        >
          {icon}
        </div>
      </div>
      <div
        className={classnames('text-center text-xs', {
          'text-accent-background-10': doesMatch,
          'text-background-10': !doesMatch,
        })}
      >
        {children}
      </div>
    </NavLink>
  );
}

export function Navbar() {
  return (
    <div className="flex flex-row xs:flex-col px-2 pt-2 flex-wrap">
      <div className="flex flex-row xs:flex-col flex-grow gap-2 h-fit">
        <NavButton to="/" icon={<CubeIcon></CubeIcon>}>
          Home
        </NavButton>
        <NavButton
          to="/onboarding/body-proportions/auto"
          match="/onboarding/body-proportions/*"
          state={{ alonePage: true }}
          icon={<GearIcon></GearIcon>}
        >
          Body proportions
        </NavButton>
        <NavButton
          to="/onboarding/trackers-assign"
          state={{ alonePage: true }}
          icon={<GearIcon></GearIcon>}
        >
          Tracker assignment
        </NavButton>
        <NavButton
          to="/onboarding/mounting/auto"
          match="/onboarding/mounting/*"
          state={{ alonePage: true }}
          icon={<GearIcon></GearIcon>}
        >
          Mounting Calibration
        </NavButton>
        <NavButton to="/onboarding/home" icon={<GearIcon></GearIcon>}>
          Setup Wizard
        </NavButton>
      </div>
      <NavButton
        to="/settings/trackers"
        match="/settings/*"
        state={{ scrollTo: 'steamvr' }}
        icon={<GearIcon></GearIcon>}
      >
        Settings
      </NavButton>
    </div>
  );
}
